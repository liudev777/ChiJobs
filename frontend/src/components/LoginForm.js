import axios from 'axios';
import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../signupstyle.css';
import { UserContext } from '../context/UserContext';

export default function LoginForm() {

    const [firstName, setFirstName] = useState(null);
    const [lastName, setLastName] = useState(null);
    const [email, setEmail] = useState(null);
    const [password,setPassword] = useState(null);
    const [error,setError] = useState(false);

    const navigate = useNavigate();

    const { setUser } = useContext(UserContext);

    const handleInputChange = (e) => {
        const {id , value} = e.target;
        if(id === "email"){
            setEmail(value);
        }
        if(id === "password"){
            setPassword(value);
        }
    }

    const handleSubmit  = async () => {
        axios.post('http://localhost:8090/login', {firstName: firstName,
                lastName: lastName,
                email: email,
                password: password
        }, { withCredentials: true })
        .then(res => {
            console.log(res)
            if(res.data === "Authorized"){
                setUser({ email: email });
                localStorage.setItem('user', JSON.stringify({email: email}));
                navigate('/home');
            }
        })
        .catch(err => {
            setError(true);
            console.log(err);
        });
    }

    return(
        <div className="form">
            {error === true &&
                <h1>Invalid Email or password</h1>
            }
            <div className="form-body">
                <div className="email">
                    <label className="form__label" for="email">Email </label>
                    <input  type="email" id="email" className="form__input" value={email} onChange = {(e) => handleInputChange(e)} placeholder="Email"/>
                </div>
                <div className="password">
                    <label className="form__label" for="password">Password </label>
                    <input className="form__input" type="password"  id="password" value={password} onChange = {(e) => handleInputChange(e)} placeholder="Password"/>
                </div>
            </div>
            <div class="footer">
                <button onClick={()=>handleSubmit()} type="submit" class="btn">Log In</button>
                <button onClick={()=>navigate('/signup')} type="submit" class="btn">Sign up</button>
            </div>
        </div>
    )
}