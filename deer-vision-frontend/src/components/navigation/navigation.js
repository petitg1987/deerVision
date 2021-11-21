import React, { Component } from 'react';
import studioLogoMini from "../../images/studioLogoMini.webp";
import { HashLink as Link } from 'react-router-hash-link';
import './navigation.css';

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
            <nav className="nav">
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
                        <Link to="/#games" className="nav-menu-link" onClick={() => this.navMenuLinkClick()}>Our Games</Link>
                    </li>
                    <li className="nav-menu-link-container">
                        <Link to="/#about-us" className="nav-menu-link" onClick={() => this.navMenuLinkClick()}>About Us</Link>
                    </li>
                </ul>
            </nav>
        );
    }
}

export default Navigation;