import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Switch, Route, Link} from 'react-router-dom';
import Home from './home';
import Games from './games';
import AboutUs from './about-us';
import './style.css';

class Index extends Component {
    render() {
        return (
            <Router>
                <div>
                    <header className="header">
                        <Link to={'/'} className="logo">Deer Vision Studio</Link>
                        <ul>
                            <li><Link to={'/games'} className="nav-link">Our Games</Link></li>
                            <li><Link to={'/about-us'} className="nav-link">About Us</Link></li>
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
