import React from 'react';
import '../App.css';
import Footer from "../components/Footer";
import Header from "../components/Header";
import SearchBar from "../components/SearchBar";
import TrendingKeywords from "../components/TrendingKeywords";

export default function Home() {
    return (
        <div className="App">
        <Header />
        <SearchBar />
        <TrendingKeywords />
        <Footer />
        </div>
    );
}
