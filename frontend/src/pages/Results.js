import axios from 'axios';
import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';

export default function Results() {
    const navigate = useNavigate();
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

    console.log(jobsArray);

    const apply  = async (jobid) => {
        console.log(jobid);
        axios.post('http://localhost:8090/apply', {jobid: jobid,
                title: " ",
                company: " ",
                location: " ",
                description: " "
        }, { withCredentials: true })
        .then(res => {
            console.log(res)
            if(res.data === "Job application Added Successful"){
                navigate('/home');
            }
        })
        .catch(err => {
            console.log(err);
        });
    }

    const bookmarkJob = (jobId) => {
        axios.post('http://localhost:8090/bookmarkJob', { jobid: jobId }, { withCredentials: true })
        .then(response => {
            console.log(response.data); // Handle the response
        })
        .catch(error => {
            console.error(error);
        });
    }

    return (
        <div>
            <h2>Job Results</h2>
            <ul>
                {jobsArray.map((job, index) => (
                    <div>
                        <li key={index}>
                            <h3>{job.title}--<Link to={`/jobs/${job.jobId}`}><button>Apply</button></Link></h3>
                            <p>{job.company}</p>
                            <p>{job.location}</p>
                            <p>{job.description}</p>
                            <button onClick={() => bookmarkJob(job.jobId)}>Bookmark</button>

                        </li>
                    </div>
                ))}
            </ul>
        </div>
    );
}
