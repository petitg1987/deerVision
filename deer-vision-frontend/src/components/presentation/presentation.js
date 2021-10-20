import React, {Component} from 'react';
import './presentation.css';
import studioLogo from "../../images/studioLogo.png";

class Presentation extends Component {

    componentDidMount() {
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('pres-text-p-anim-trigger');
                } else {
                    entry.target.classList.remove('pres-text-p-anim-trigger');
                }
            });
        });
        const paragraphes = document.querySelectorAll('.pres-text p');
        paragraphes.forEach(e => observer.observe(e));
    }

    render() {
        return (
            <div className="pres-container">
                <div className="pres-separator"/>
                <div className="pres-text">
                    <p>Deer Vision Studio is an independent studio, which develops video games. The studio is located in Belgium (Ardenne).</p>
                    <p>Our philosophy is to publish high quality games and listen our community to constantly improve our games in the right direction. Therefore, it is always a real pleasure to discuss with you on our social networks (see links below).</p>
                    <p>We like to create our games without any technical restrictions. This is why we have a home-made game engine named "Urchin Engine" that we have been developing for more than 10 years. The engine is free and open source: <a href="https://github.com/petitg1987/urchinEngine" target="_blank" rel="noopener noreferrer">GitHub</a></p>
                </div>
                <div className="pres-separator"/>
                <div className="pres-logo">
                    <img src={studioLogo} alt="Studio Logo" width="300" height="300"/>
                </div>
            </div>
        );
    }
}

export default Presentation;
