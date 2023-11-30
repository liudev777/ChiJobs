import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../signupstyle.css';

export default function ProfileNavigate() {

    const navigate = useNavigate();

    const goToProfile = () => {
        // Use navigate to go to the user profile page
        navigate('/profile');
    };

    return(
        <div className="form">
            <div class="footer">
                <button onClick={goToProfile} type="submit" class="btn">Profile</button>
            </div>
        </div>
    )
}