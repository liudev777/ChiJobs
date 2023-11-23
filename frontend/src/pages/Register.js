import React from 'react';
import '../App.css';
import Footer from "../components/Footer";
import Header from "../components/Header";
import RegistrationForm from "../components/RegistrationForm";

export default function Home() {
    return (
        <div>
        <Header />
        <RegistrationForm />
        <Footer />
        </div>
    );
}
