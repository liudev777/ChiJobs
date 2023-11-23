import axios from 'axios';
import { React, useState } from 'react';
import { useNavigate } from 'react-router-dom';
var data = require("../data.json");

export default function AutoComplete() {
    const [keywords, setKeywords] = useState('');
    const [zipcode, setZipcode] = useState('');
    const navigate = useNavigate(); // For navigation

    const [loading, setLoading] = useState(false);

    const onChange= (event) =>{
        setKeywords(event.target.value);
    }


    const handleSearch = async () => {
        setLoading(true);
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
    

    return (
        <div className='search-section'>
            <input
                type="text"
                placeholder="Job title, Company..."
                style={{ margin: "10px" }}
                onChange={onchange}
            />
            <div className="dropdown">
                {data.jobTitles.filter(item => {
                    const searchTerm = keywords.toLowerCase();
                    const fullName = item.toLowerCase();

                    return searchTerm && fullName.startsWith(searchTerm)
                })
                .map((item) => (
                    <div className="dropdown-row">{item}</div>
                ))}
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
        </div>
    );
}