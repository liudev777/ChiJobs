import React from 'react';
import { useLocation } from 'react-router-dom';

export default function Results() {
    const location = useLocation();
    const { data, loading } = location.state || {};

    if (loading) {
        return <p>Loading...</p>;
    }

    // Check if data is present and is an object
    if (!data || typeof data !== 'object') {
        return <p>No results found.</p>;
    }

    // Convert object to array
    const jobsArray = Object.values(data);

    return (
        <div>
            <h2>Job Results</h2>
            <ul>
                {jobsArray.map((job, index) => (
                    <li key={index}>
                        <h3>{job.title}</h3>
                        <p>{job.company}</p>
                        <p>{job.location}</p>
                        <p>{job.description}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
}
