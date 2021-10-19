import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Switch, Route, Link} from 'react-router-dom';
import Home from './home';
import Games from './games';
import AboutUs from './about-us';
import Admin from './admin';
import './style.css';
import studioLogoMini from './images/studioLogoMini.png'

class Index extends Component {

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
            <Router>
                <div>
                    <header>
                        <nav className="nav">
                            <div className="nav-logo-and-hamburger">
                                <Link to={'/'}>
                                    <img className="nav-logo" src={studioLogoMini} alt="Studio Logo" width="60"
                                         height="60"/>
                                    <div className="nav-logo-text">Deer Vision Studio</div>
                                </Link>
                                <Link to="#" className="nav-hamburger-icon" onClick={() => this.navMenuIconClick()}>
                                    <svg viewBox="0 0 100 80" width="23" height="18.4">
                                        <rect width="100" height="20" rx="8"/>
                                        <rect y="30" width="100" height="20" rx="8"/>
                                        <rect y="60" width="100" height="20" rx="8"/>
                                    </svg>
                                </Link>
                            </div>
                            <span className="nav-spacing">&nbsp;</span>
                            <ul className="nav-menu" id="nav-menu-id">
                                <li className="nav-menu-link-container">
                                    <Link to={'/about-us'} className="nav-menu-link"
                                          onClick={() => this.navMenuLinkClick()}>About Us</Link>
                                </li>
                                <li className="nav-menu-link-container">
                                    <Link to={'/games'} className="nav-menu-link"
                                          onClick={() => this.navMenuLinkClick()}>Our Games</Link>
                                </li>
                            </ul>
                        </nav>
                    </header>
                    <div className="content">
                        <Switch>
                            <Route exact path='/' component={Home}/>
                            <Route path='/games' component={Games}/>
                            <Route path='/about-us' component={AboutUs}/>
                            <Route path='/admin' component={Admin}/>
                        </Switch>
                    </div>
                </div>
            </Router>
        );
    }
}

ReactDOM.render((
    <Index/>
), document.getElementById('app'))
