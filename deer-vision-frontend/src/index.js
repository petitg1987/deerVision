import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Switch, Route, Link} from 'react-router-dom';
import Home from './home';
import Games from './games';
import AboutUs from './about-us';
import './style.css';

class Index extends Component {

    menuIconClick() {
        let menu = document.getElementById("menuId");
        if (menu.classList.contains("responsive")) {
            menu.classList.remove("responsive");
        } else {
            menu.classList.add("responsive");
        }
    }

    menuLinkClick() {
        let menu = document.getElementById("menuId");
        menu.classList.remove("responsive");
    }

    render() {
        return (
            <Router>
                <div>
                    <header className="header">
                        <div className="nav-left">
                            <Link to={'/'} className="nav-logo">Deer Vision Studio</Link>
                            <Link to="#" className="menu-icon" onClick={() => this.menuIconClick()}>
                                <svg viewBox="0 0 100 80" width="16" height="16">
                                    <rect width="100" height="20" rx="8"/>
                                    <rect y="30" width="100" height="20" rx="8"/>
                                    <rect y="60" width="100" height="20" rx="8"/>
                                </svg>
                            </Link>
                        </div>
                        <ul className="menu" id="menuId">
                            <li className="nav-link-container">
                                <Link to={'/about-us'} className="nav-link" onClick={() => this.menuLinkClick()}>About Us</Link>
                            </li>
                            <li className="nav-link-container">
                                <Link to={'/games'} className="nav-link" onClick={() => this.menuLinkClick()}>Our Games</Link>
                            </li>
                        </ul>
                    </header>
                    <div className="content">
                        <Switch>
                            <Route exact path='/' component={Home}/>
                            <Route path='/games' component={Games}/>
                            <Route path='/about-us' component={AboutUs}/>
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
