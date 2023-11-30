import React from 'react';
import { useLocation } from "react-router-dom";
import Logout from './Logout';
import ProfileNavigate from './ProfileNavigate';

export default function Header() {
    const currentLocation = useLocation();
    var match = false;
    var profile_match = false;

    if (currentLocation.pathname === "/login"){
        match = true
    }
    if (currentLocation.pathname === "/signup"){
        match = true
    }
    if (currentLocation.pathname === "/profile"){
        profile_match = true
    }
    

    return(
        <header>
            <h1>CHIJOBS</h1>
            {!!!profile_match &&
                <ProfileNavigate></ProfileNavigate>
            }
            {!!!match &&
                <Logout></Logout>
            }
        </header>
    )
}
