import axios from 'axios';
import React from "react";
import { useNavigate, useParams } from "react-router-dom";

export default function JobDetail() {
    const navigate = useNavigate();
    const params = useParams()
    const [job, setJob] = React.useState(null)
    const [applied, setApplied] = React.useState(false)
    const [bookmarked, setBookmarked] = React.useState(false)

    React.useEffect(() => {
        axios.post('http://localhost:8090/getJob', {
            jobid: params.id,
            title: " ",
            company: " ",
            location: " ",
            description: " ",
        }, { withCredentials: true })
        .then(res => {
            console.log(res.data.jobid);
            setJob(res.data);
            checkApplicationStatus(res.data.jobid);
        })
        .catch(err => {
            console.log(err);
        });

        axios.post('http://localhost:8090/checkBookmarkStatus', {
            jobid: params.id,
            title: " ",
            company: " ",
            location: " ",
            description: " ",
        }, { withCredentials: true })
        .then(res => {
            console.log(res);
            setBookmarked(res.data);
        })
        .catch(err => {
            console.log(err);
        });
    }, [params.id]);

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

    const toggleBookmark = () => {
        // Check if the job is already bookmarked
        if (!bookmarked){
            axios.post('http://localhost:8090/bookmarkJob', {jobid: params.id,
                    title: " ",
                    company: " ",
                    location: " ",
                    description: " "
            }, { withCredentials: true })
            .then(res => {
                console.log(res.data);
                setBookmarked(!bookmarked);
            })
            .catch(err => {
                console.log(err);
            });
        }
        else {
            axios.post('http://localhost:8090/unbookmarkJob', {jobid: params.id,
                    title: " ",
                    company: " ",
                    location: " ",
                    description: " "
            }, { withCredentials: true })
            .then(res => {
                console.log(res.data);
                // If unbookmarking is successful, update the local state
                setBookmarked(false);
            })
            .catch(err => {
                console.log(err);
            });
        }
    };

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
                    </button>
                        <button
                            onClick={toggleBookmark}
                            style={{
                                marginLeft: '10px',
                                backgroundColor: bookmarked ? 'gray' : 'green',
                            }}
                        >
                            {bookmarked ? "Bookmarked" : "Bookmark"}
                        </button>
                    </h2>
                    <h3>Position: {job.title}</h3>
                    <p>Location: {job.location}</p>
                    <p>Description: {job.description}</p>
                </div>
            ) : <h2>Loading...</h2>}
        </div>
    )
}