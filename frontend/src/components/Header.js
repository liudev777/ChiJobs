import React from 'react';
import { useLocation } from "react-router-dom";
import Logout from './Logout';

export default function Header() {
    const currentLocation = useLocation();
    var match = false;

    if (currentLocation.pathname === "/login"){
        match = true
    }
    if (currentLocation.pathname === "/signup"){
        match = true
    }
    

    return(
        <header>
            <h1>CHIJOBS</h1>
            <nav></nav>
            {!!!match &&
                <Logout></Logout>
            }
        </header>
    )
}
