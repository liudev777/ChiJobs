import axios from 'axios';
import React from "react";
import { useNavigate, useParams } from "react-router-dom";

export default function JobDetail() {
    const navigate = useNavigate();
    const params = useParams()
    const [job, setJob] = React.useState(null)
    const [applied, setApplied] = React.useState(false)

    React.useEffect(() => {
        axios.post('http://localhost:8090/getJob', {jobid: params.id,
                title: " ",
                company: " ",
                location: " ",
                description: " "
        }, { withCredentials: true })
        .then(res => {
            console.log(res.data.jobid)
            setJob(res.data)
            checkApplicationStatus(res.data.jobid)
        })
        .catch(err => {
            console.log(err);
        })
    }, [params.id])

    const checkApplicationStatus = async (jobid) => {
        axios.post('http://localhost:8090/checkApplication', {jobid: jobid,
        title: " ",
        company: " ",
        location: " ",
        description: " "
        }, { withCredentials: true })
        .then(res => {
            // console.log(res.data)
            setApplied(true);
        })
        .catch(err => {
            console.log(err);
            setApplied(false);
        });
    };

    const apply  = async () => {
        axios.post('http://localhost:8090/apply', {jobid: params.id,
                title: " ",
                company: " ",
                location: " ",
                description: " "
        }, { withCredentials: true })
        .then(res => {
            if(res.data === "Added Successfully"){
                setApplied(true);
                // navigate('/results');
            }
        })
        .catch(err => {
            console.log(err);
        });
    }

    const bookmarkJob = () => {
        axios.post('http://localhost:8090/bookmarkJob', { jobid: params.id }, { withCredentials: true })
        .then(response => {
            console.log(response.data); // Handle the response
        })
        .catch(error => {
            console.error(error);
        });
    }

    return (
        <div>
            {job ? (
                <div>
                    <h2>{job.company}--<button
                        onClick={() => applied ? null : apply()}
                        disabled={applied}
                        style={{
                            cursor: applied ? 'default' : 'pointer',
                            backgroundColor: applied ? 'gray' : 'green',
                            color: '#fff', // Set text color to white
                        }}
                    >
                        {applied ? "Applied" : "Apply"}
                    </button></h2>
                    <h3>Position: {job.title}</h3>
                    <p>Location: {job.location}</p>
                    <p>Description: {job.description}</p>
                    <button onClick={bookmarkJob}>Bookmark</button>
                </div>
            ) : <h2>Loading...</h2>}
        </div>
    )
}