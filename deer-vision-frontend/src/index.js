import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Switch, Route} from 'react-router-dom';
import Main from './pages/main/main';
import Admin from './pages/admin/admin';
import Navigation from "./components/navigation/navigation";
import './index.css';

class Index extends Component {
    render() {
        return (
            <BrowserRouter>
                <div>
                    <header>
                        <Navigation/>
                    </header>
                    <div className="content">
                        <Switch>
                            <Route exact path='/' component={Main}/>
                            <Route exact path='/admin' component={Admin}/>
                        </Switch>
                    </div>
                </div>
            </BrowserRouter>
        );
    }
}

ReactDOM.render((
    <Index/>
), document.getElementById('app'))
