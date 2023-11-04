import React from "react"
import { useParams } from "react-router-dom"

export default function VanDetail() {
    const params = useParams()
    const [job, setJob] = React.useState(null)

    React.useEffect(() => {
        fetch(`/api/jobs/${params.id}`)
            .then(res => res.json())
            .then(data => setJob(data.jobs))
    }, [params.id])

    return (
        <div>
            {job ? (
                <div>
                    <h3>{job.company}</h3>
                    <p>Position: {job.position}</p>
                    <p>Level: {job.level}</p>
                    <p>Location: {job.location}</p>
                    <p>{job.languages}</p>
                    <p>{job.contract}</p>
                </div>
            ) : <h2>Loading...</h2>}
        </div>
    )
}