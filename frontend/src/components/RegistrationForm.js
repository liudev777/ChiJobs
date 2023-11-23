import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../signupstyle.css';

export default function RegistrationForm() {
    
    const [firstName, setFirstName] = useState(null);
    const [lastName, setLastName] = useState(null);
    const [email, setEmail] = useState(null);
    const [password,setPassword] = useState(null);
    const [confirmPassword,setConfirmPassword] = useState(null);
    const [samepassword,setSamepassword] = useState(true);
    const [error,setError] = useState(false);

    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const {id , value} = e.target;
        if(id === "firstName"){
            setFirstName(value);
        }
        if(id === "lastName"){
            setLastName(value);
        }
        if(id === "email"){
            setEmail(value);
        }
        if(id === "password"){
            setPassword(value);
        }
        if(id === "confirmPassword"){
            setConfirmPassword(value);
        }

    }

    const handleSubmit  = async () => {
        if (password === confirmPassword)
        {
            setSamepassword(true);
            axios.post('http://localhost:8090/signup', {firstName: firstName,
                    lastName: lastName,
                    email: email,
                    password: password
                    
            })
            .then(res => {
                console.log(res)
                if(res.data === "Added Successfully"){
                    navigate('/login');
                }
            })
            .catch(err => {
                setError(true);
                console.log(err);
            });
        }
        else{
            setSamepassword(false);
        }
    }

    return(
        <div className="form">
            {samepassword === false &&
                <h2>Password is not the same. Try Again.</h2>
            }
            {error === true &&
                <h1>User already exist. Enter a new email</h1>
            }
        <div className="form-body">
            <div className="username">
                <label className="form__label" for="firstName">First Name </label>
                <input className="form__input" type="text" value={firstName} onChange = {(e) => handleInputChange(e)} id="firstName" placeholder="First Name"/>
            </div>
            <div className="lastname">
                <label className="form__label" for="lastName">Last Name </label>
                <input  type="text" name="" id="lastName" value={lastName}  className="form__input" onChange = {(e) => handleInputChange(e)} placeholder="LastName"/>
            </div>
            <div className="email">
                <label className="form__label" for="email">Email </label>
                <input  type="email" id="email" className="form__input" value={email} onChange = {(e) => handleInputChange(e)} placeholder="Email"/>
            </div>
            <div className="password">
                <label className="form__label" for="password">Password </label>
                <input className="form__input" type="password"  id="password" value={password} onChange = {(e) => handleInputChange(e)} placeholder="Password"/>
            </div>
            <div className="confirm-password">
                <label className="form__label" for="confirmPassword">Confirm Password </label>
                <input className="form__input" type="password" id="confirmPassword" value={confirmPassword} onChange = {(e) => handleInputChange(e)} placeholder="Confirm Password"/>
            </div>
        </div>
        <div class="footer">
            <button onClick={()=>handleSubmit()} type="submit" class="btn">Register</button>
        </div>
    </div>
    )
}