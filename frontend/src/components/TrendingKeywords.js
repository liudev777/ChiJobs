import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function TrendingKeywords() {
    const [trendingKeywords, setTrendingKeywords] = useState([]);

    useEffect(() => {
        const fetchTrendingKeywords = async () => {
            try {
                const response = await axios.get('http://localhost:8090/top-queries');
                setTrendingKeywords(response.data);
            } catch (error) {
                console.error('Error fetching trending keywords:', error);
            }
        };

        fetchTrendingKeywords();
    }, []); 

    return (
        <div className='trending-keyword-section'>
            <h2>Trending Keywords:</h2>
            <ul>
                {trendingKeywords.map((keyword, index) => (
                    <li key={index}>{keyword}</li>
                ))}
            </ul>
        </div>
    );
}
