import React from 'react';
import '../App.css';
import Footer from "../components/Footer";
import Header from "../components/Header";
import LoginForm from "../components/LoginForm";

export default function Home() {
    return (
        <div>
        <Header />
        <LoginForm />
        <Footer />
        </div>
    );
}
