import axios from 'axios';
import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../signupstyle.css';

export default function Logout() {

    const navigate = useNavigate();

    const handleSubmit  = async () => {
        axios.post('http://localhost:8090/logout', {value:""}, { withCredentials: true })
        .then(res => {
            if(res.data === "Logged Out"){
                navigate('/login');
            }
        })
        .catch(err => {
            console.log(err);
        });
    }

    return(
        <div className="form">
            <div class="footer">
                <button onClick={()=>handleSubmit()} type="submit" class="btn">Log Out</button>
            </div>
        </div>
    )
}