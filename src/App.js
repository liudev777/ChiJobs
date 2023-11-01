import React from 'react';
import './App.css';

function App() {
  return (
    <div className="App">
      <Header />
      <SearchBar />
      <TrendingKeywords />
      <Footer />
    </div>
  );
}

function Header() {
  return (
    <header>
      <h1>CHIJOBS</h1>
      <nav>
      </nav>
    </header>
  );
}

function SearchBar() {
  return (
    <div className='search-section'>
      <input type="text" placeholder="Job title, Company..." style={{margin:"10px"}}/>
      <select name="remote" style={{margin:"10px"}}>
        <option value="select-work-preference">Select Work Preference</option>
        <option value="">Remote</option>
        <option value="">Hybrid</option>
        <option value="">In-Person</option>
        {/* Add more options */}
      </select>
      <select name="job-type" style={{margin:"10px"}}>
        <option value="select-type">Select Job Type</option>
        <option value="full-time">Full time</option>
        <option value="part-time">Part time</option>
        <option value="contract">Contract</option>
        <option value="internship">Internship</option>
        <option value="temporary">Temporary</option>
        {/* Add more options */}
      </select>
      <select name="date-posted" style={{margin:"10px"}}>
        <option value="date-posted">Select Date Posted</option>
        <option value="1-day">1 day</option>
        <option value="3-day">3 day</option>
        <option value="1-week">1 week</option>
        <option value="1-month">1 month</option>
      </select>
      <button style={{margin:"10px"}}>Search Job</button>
    </div>
  );
}

function TrendingKeywords() {
  return (
    <div className='trending-keyword-section'>
      <h2>Trending Keywords:</h2>
      {/* Render trending keywords */}
    </div>
  );
}

function Footer() {
  return (
    <footer className="app-footer">
      <p>© 2023 CHIJOBS. All rights reserved.</p>
      <nav>
        <a href="#">Terms of Service</a>
        <a href="#">Privacy Policy</a>
        <a href="#">Contact Us</a>
      </nav>
    </footer>
  );
}



export default App;
