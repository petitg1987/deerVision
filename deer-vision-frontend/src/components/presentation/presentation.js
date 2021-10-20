import React, {Component} from 'react';
import './presentation.css';
import studioLogo from "../../images/studioLogo.png";

class Presentation extends Component {

    componentDidMount() {
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('pres-text-anim-trigger');
                } else {
                    entry.target.classList.remove('pres-text-anim-trigger');
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
                    <p>Binogure Studio is an independent video game studio based at Orthez (France). Our goal is to create brilliant, high quality games.</p>
                    <p>We love free software, as in free beer but also as in freedom. It means that to make our games we only use FOSS software: Blender for 3D modeling or video editing; Krita and Inkscape for art and game assets; and Godot Engine to create a game.</p>
                    <p>We sell games, and we sell games only. You won't see any ad in our games. And because we are all gamer, we won't ever create any pay2win game. Our economic model is based on the play2win mechanism.</p>
                    <p>Since you get our games to play and not to gamble, you won't have to pay for loot-boxes containing random rewards. It might exist in some game, but you will get them for free.</p>
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
