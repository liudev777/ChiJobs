import axios from 'axios';
import React, { useEffect, useState } from 'react';
import '../App.css';
import Footer from "../components/Footer";
import Header from "../components/Header";
import SearchBar from "../components/SearchBar";
import TrendingKeywords from "../components/TrendingKeywords";
import neo4jService from '../neo4jService';
import Recommend from './Recommend';
import { useNavigate } from 'react-router-dom';

export default function Home() {
      
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Fetch all applications
                const response = await axios.get('http://localhost:8090/getAllApplications', { withCredentials: true });
                const fetchedApplications = response.data;

                // Update the state with fetched applications
                setApplications(fetchedApplications);

                // Load applications into Neo4j
                console.log(fetchedApplications);
                await neo4jService.insertApplications(fetchedApplications);
                console.log('Applications loaded into Neo4j successfully');
            } catch (error) {
                console.error('Error fetching or loading applications:', error);
            } finally {
                neo4jService.close();
            }
        };

        // Call the fetchData function
        fetchData();
    }, []);

    const handleClick = async () => {
        setLoading(true);
        try {
            const res = await axios.get('http://localhost:8090/getTrend');
            navigate('/report', { state: { data: res.data, loading: false } });
        } catch (error) {
            console.error('Error fetching data: ', error);
            setLoading(false);
        }
      };

    return (
        <div className="App">
        <Header />
        <SearchBar />
        <Recommend />
        <button onClick={handleClick}>Report</button>
        <TrendingKeywords />
        <Footer />
        </div>
    );
}
