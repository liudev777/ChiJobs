import axios from 'axios';
import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const SessionChecker = () => {
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        const checkSession = async () => {
        try {
            // Check if the current route is not "/signup"
            if (location.pathname !== '/signup') {
                const response = await axios.get('http://localhost:8090/check-session',{ withCredentials: true });
                const { sessionValid } = response.data;

                if (!sessionValid) {
                // Redirect to the login page if session is not valid
                navigate('/login');
                }
            }
        } catch (error) {
            console.error('Error checking session:', error);
            // Handle error if necessary
        }
        };

        checkSession();
    }, [navigate]);

  return null; // No need to render anything
};

export default SessionChecker;
