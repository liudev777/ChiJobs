import axios from 'axios';
import { React, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function SearchBar() {
    const [keywords, setKeywords] = useState('');
    const [zipcode, setZipcode] = useState('');
    const navigate = useNavigate();
    const [allJobTitles, setAllJobTitles] = useState([]);
    const [suggestions, setSuggestions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [isZipCodeFetched, setIsZipCodeFetched] = useState(false);


    useEffect(() => {
        fetchAllJobTitles();
    }, []);

    const fetchAllJobTitles = async () => {
        try {
            const response = await axios.get('http://localhost:8090/getJobTitles');
            setAllJobTitles(response.data);
        } catch (error) {
            console.error('Error fetching all job titles: ', error);
        }
    };

    useEffect(() => {
        if (zipcode && isZipCodeFetched) {
            handleSearch();
            setIsZipCodeFetched(false); // Reset the flag
        }
    }, [zipcode, isZipCodeFetched]);

    const fetchZipCode = async (lat, lng) => {
        const username = process.env.REACT_APP_GEOAPI; 
        try {
            const response = await axios.get(`http://api.geonames.org/findNearbyPostalCodesJSON?lat=${lat}&lng=${lng}&username=${username}`);
            if (response.data.postalCodes.length > 0) {
                setZipcode(response.data.postalCodes[0].postalCode);
                setIsZipCodeFetched(true); 
                console.log(response.data.postalCodes[0].postalCode);
            } else {
                console.error('No postal codes found');
            }
        } catch (error) {
            console.error('Error fetching postal code:', error);
        }
    };
    
    const handleNearMeClick = () => {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                fetchZipCode(position.coords.latitude, position.coords.longitude);
            },
            (error) => console.error(error),
            { enableHighAccuracy: true }
        );
    };

    const handleSearch = async () => {
        setLoading(true);
        console.log("zipcode: " + zipcode);
        try {
            const res = await axios.get('http://localhost:8090/searchJobs', {
                params: { keyword: keywords, zipcode: zipcode }
            });
            navigate('/results', { state: { data: res.data, loading: false } });
        } catch (error) {
            console.error('Error fetching data: ', error);
            setLoading(false);
        }
    };

    const handleKeywordChange = (e) => {
        const input = e.target.value;
        setKeywords(input);

        if (input.trim() === '') {
            setSuggestions([]);
            return;
        }

        const filteredSuggestions = allJobTitles.filter(
            (title) => title.toLowerCase().includes(input.toLowerCase())
        );

        setSuggestions(filteredSuggestions);
    };

    const handleSuggestionClick = (suggestion) => {
        setKeywords(suggestion);

        setSuggestions([]);
    };

    return (
        <div className='search-section'>
            <div className="search-bar-container">
                <input
                    type="text"
                    placeholder="Job title, Company..."
                    style={{ margin: "10px" }}
                    value={keywords}
                    onChange={handleKeywordChange}
                />
                {suggestions.length > 0 && keywords.trim() !== '' && (
                    <ul className="suggestions-list">
                        {suggestions.map((suggestion, index) => (
                            <li key={index} onClick={() => handleSuggestionClick(suggestion)}>
                                {suggestion}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
            <input
                type="text"
                placeholder="Zipcode"
                style={{ margin: "10px" }}
                onChange={(e) => setZipcode(e.target.value)}
            />
            <button
                style={{ margin: "10px" }}
                onClick={handleSearch}
            >
                Search Job
            </button>
            <button
                style={{ margin: "10px" }}
                onClick={handleNearMeClick}
            >
                Near Me
            </button>
        </div>
    );
}
