import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Footer from "../components/Footer";
import Header from "../components/Header";

export default function Profile() {
    const [appliedJobs, setAppliedJobs] = useState([]);
    const [bookmarkedJobs, setBookmarkedJobs] = useState([]);

    useEffect(() => {
        // Fetch applied jobs from the backend
        axios.get('http://localhost:8090/getjobsApplied', { withCredentials: true })
            .then(res => {
                console.log(res.data);
                setAppliedJobs(res.data);
            })
            .catch(err => {
                console.log(err);
            });

        // Fetch bookmarked jobs from the backend
        axios.get('http://localhost:8090/getJobsBookmarked', { withCredentials: true })
            .then(res => {
                console.log(res.data);
                setBookmarkedJobs(res.data);
            })
            .catch(err => {
                console.log(err);
            });
    }, []);

    return (
        <div>
            <Header />
            <section>
                <h3>Applied Jobs</h3>
                <ul>
                    {appliedJobs.map(job => (
                        <li key={job.jobId}>
                            <Link to={`/jobs/${job.jobId}`}>{job.title} - {job.company}</Link>
                        </li>
                    ))}
                </ul>
            </section>

            <section>
                <h3>Bookmarked Jobs</h3>
                <ul>
                    {bookmarkedJobs.map(job => (
                        <li key={job.jobId}>
                            <Link to={`/jobs/${job.jobId}`}>{job.title} - {job.company}</Link>
                        </li>
                    ))}
                </ul>
            </section>
            <Footer />
        </div>
    );
}
