import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';
import Home from './pages/home';
import Games from './pages/games';
import AboutUs from './pages/about-us';
import Admin from './pages/admin';
import Navigation from "./components/navigation/navigation";
import './index.css';

class Index extends Component {
    render() {
        return (
            <Router>
                <div>
                    <header>
                        <Navigation/>
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
