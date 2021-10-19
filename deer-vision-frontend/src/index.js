import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';
import Home from './home';
import Games from './games';
import AboutUs from './about-us';
import Admin from './admin';
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
