import React, { Component } from 'react';
import studioLogoMini from "../../images/studioLogoMini.webp";
import { HashLink as Link } from 'react-router-hash-link';
import './navigation.css';

let prevScrollPos = window.pageYOffset;
window.onscroll = function() {
    if (window.matchMedia("(max-width: 800px)").matches) {
        let currentScrollPos = window.pageYOffset;
        if (prevScrollPos > currentScrollPos + 2 || document.scrollTop === 0) {
            document.getElementById("nav-id").style.opacity = "1.0";
            prevScrollPos = currentScrollPos;
        } else if (prevScrollPos < currentScrollPos - 10) {
            document.getElementById("nav-id").style.opacity = "0.0";
            prevScrollPos = currentScrollPos;
        }
    }
}

class Navigation extends Component {

    navMenuIconClick(evt) {
        evt.preventDefault();
        let menu = document.getElementById("nav-menu-id");
        if (menu.classList.contains("responsive")) {
            menu.classList.remove("responsive");
        } else {
            menu.classList.add("responsive");
        }
    }

    navMenuLinkClick() {
        let menu = document.getElementById("nav-menu-id");
        menu.classList.remove("responsive");
    }

    render() {
        return (
            <nav className="nav" id="nav-id">
                <div className="nav-logo-and-hamburger">
                    <a href="/#">
                        <img className="nav-logo" src={studioLogoMini} alt="Studio Logo" width="61" height="61"/>
                        <div className="nav-logo-text">Deer Vision Studio</div>
                    </a>
                    <a href="/" className="nav-hamburger-icon" onClick={(evt) => this.navMenuIconClick(evt)}>
                        <svg viewBox="0 0 100 100" width="23" height="21">
                            <rect fill="#ffffff" y="0"  width="100" height="21" rx="8"/>
                            <rect fill="#ffffff" y="39" width="100" height="21" rx="8"/>
                            <rect fill="#ffffff" y="79" width="100" height="21" rx="8"/>
                        </svg>
                    </a>
                </div>
                <div className="nav-spacing">&nbsp;</div>
                <ul className="nav-menu-ul" id="nav-menu-id">
                    <li className="nav-menu-link-container">
                        <Link to="/" className="nav-menu-link" onClick={() => this.navMenuLinkClick()}>Games</Link>
                    </li>
                    <li className="nav-menu-link-container">
                        <Link to="/about" className="nav-menu-link" onClick={() => this.navMenuLinkClick()}>About</Link>
                    </li>
                </ul>
            </nav>
        );
    }
}

export default Navigation;