import React, { Component } from 'react';
import studioLogoMini from "../../images/studioLogoMini.png";
import { HashLink as Link } from 'react-router-hash-link';
import './navigation.css';

class Navigation extends Component {

    navMenuIconClick() {
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
                    <Link to="/#">
                        <img className="nav-logo" src={studioLogoMini} alt="Studio Logo" width="60" height="60"/>
                        <div className="nav-logo-text">Deer Vision Studio</div>
                    </Link>
                    <Link to="#" className="nav-hamburger-icon" onClick={() => this.navMenuIconClick()}>
                        <svg viewBox="0 0 100 80" width="23" height="18.4">
                            <rect fill="#ffffff" y="0"  width="100" height="18" rx="8"/>
                            <rect fill="#ffffff" y="30" width="100" height="18" rx="8"/>
                            <rect fill="#ffffff" y="60" width="100" height="18" rx="8"/>
                        </svg>
                    </Link>
                </div>
                <div className="nav-spacing">&nbsp;</div>
                <ul className="nav-menu-ul" id="nav-menu-id">
                    <li className="nav-menu-link-container">
                        <Link to="/#games" className="nav-menu-link" onClick={() => this.navMenuLinkClick()}>Our Games</Link>
                    </li>
                    <li className="nav-menu-link-container">
                        <Link to="/#aboutUs" className="nav-menu-link" onClick={() => this.navMenuLinkClick()}>About Us</Link>
                    </li>
                </ul>
            </nav>
        );
    }
}

export default Navigation;