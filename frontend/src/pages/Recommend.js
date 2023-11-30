import React, { useState, useContext } from 'react'
import { UserContext } from '../context/UserContext'
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Results from './Results';

function Recommend() {

  const { user } = useContext(UserContext);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleClick = async () => {
    setLoading(true);
    try {
        const res = await axios.get('http://localhost:8090/recommendJobs', {
            params: { userEmail: user.email }
        });
        navigate('/results', { state: { data: res.data, loading: false } });
    } catch (error) {
        console.error('Error fetching data: ', error);
        setLoading(false);
    }
  };

  return (
    <div>
      <button onClick={handleClick}>Recommended Jobs</button>
    </div>
  )
}

export default Recommend