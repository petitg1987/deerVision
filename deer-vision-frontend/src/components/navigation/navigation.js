import React, { Component } from 'react';
import studioLogoMini from "../../images/studioLogoMini.webp";
import { HashLink as Link } from 'react-router-hash-link';
import './navigation.css';

let prevScrollPos = window.scrollX;
window.onscroll = function() {
    if (window.matchMedia("(max-width: 800px)").matches) {
        let currentScrollPos = window.scrollX;
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
        if (menu.classList.contains("opened")) {
            menu.classList.remove("opened");
        } else {
            menu.classList.add("opened");
        }
    }

    navMenuLinkClick() {
        let menu = document.getElementById("nav-menu-id");
        menu.classList.remove("opened");
    }

    render() {
        return (
            <nav className="nav" id="nav-id">
                <div className="nav-main-container">
                    <a href="/#" className="nav-logo">
                        <img className="nav-logo-img" src={studioLogoMini} alt="Studio Logo" width="61" height="61"/>
                        <div className="nav-logo-text">Deer Vision Studio</div>
                    </a>

                    <div className="nav-hamburger">
                        <a href="/" aria-label={"menu"} className="nav-hamburger-icon" onClick={(evt) => this.navMenuIconClick(evt)}>
                            <svg viewBox="0 0 100 100" width="23" height="21">
                                <rect fill="#ffffff" y="0"  width="100" height="21" rx="8"/>
                                <rect fill="#ffffff" y="39" width="100" height="21" rx="8"/>
                                <rect fill="#ffffff" y="79" width="100" height="21" rx="8"/>
                            </svg>
                        </a>
                    </div>
                </div>
                <div className="nav-links-container">
                    <ul className="nav-links-ul" id="nav-menu-id">
                        <li className="nav-link-li">
                            <Link to="/" className="nav-link" onClick={() => this.navMenuLinkClick()}>Games</Link>
                        </li>
                        <li className="nav-link-li">
                            <Link to="/about" className="nav-link" onClick={() => this.navMenuLinkClick()}>About</Link>
                        </li>
                    </ul>
                </div>
            </nav>
        );
    }
}

export default Navigation;