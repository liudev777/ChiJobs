import React from "react";
import { Link } from "react-router-dom";
import Footer from "../components/Footer";
import Header from "../components/Header";

export default function Jobs() {
    const [jobs, setjobs] = React.useState([])
    React.useEffect(() => {
        fetch("/api/jobs")
            .then(res => res.json())
            .then(data => setjobs(data.jobs))
    }, [])

    const jobElements = jobs.map(job => (
        <div key={job.id}>
            <div>
            <Link to={`/jobs/${job.id}`}>
                <h3>{job.company}</h3>
                <p>Position: {job.position}</p>
                <p>Level: {job.level}</p>
                <p>Location: {job.location}</p>
                <p>{job.languages}</p>
                <p>{job.contract}</p>
            </Link>
            </div>
        </div>
    ))

    return (
        <div>
            <Header />
            <h1>Explore Jobs</h1>
            <div>
                {jobElements}
            </div>
            <Footer />
        </div>
    )
}