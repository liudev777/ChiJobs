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

    useEffect(() => {
        // Fetch all job titles when the component mounts
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

    const handleKeywordChange = (e) => {
        const input = e.target.value;
        setKeywords(input);

        // Clear suggestions if input is empty
        if (input.trim() === '') {
            setSuggestions([]);
            return;
        }

        // Filter job titles based on input locally
        const filteredSuggestions = allJobTitles.filter(
            (title) => title.toLowerCase().includes(input.toLowerCase())
        );

        // Update the suggestions
        setSuggestions(filteredSuggestions);
    };

    const handleSuggestionClick = (suggestion) => {
        // Set the selected suggestion as the input value
        setKeywords(suggestion);

        // Clear the suggestions
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
                {/* Display suggestions only if there are suggestions and the input is not empty */}
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
        </div>
    );
}
