import React, {Component, Suspense} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Switch, Route} from 'react-router-dom';
import Navigation from "./components/navigation/navigation";
import './index.css';

const Main = React.lazy(() => import('./pages/main/main'))
const Admin = React.lazy(() => import('./pages/admin/admin'))

class Index extends Component {
    render() {
        return (
            <BrowserRouter>
                <div>
                    <header>
                        <Navigation/>
                    </header>
                    <div className="content">
                        <Suspense fallback={<div>Loading...</div>}>
                            <Switch>
                                <Route exact path='/' component={Main}/>
                                <Route exact path='/admin' component={Admin}/>
                            </Switch>
                        </Suspense>
                    </div>
                </div>
            </BrowserRouter>
        );
    }
}

ReactDOM.render((
    <Index/>
), document.getElementById('app'))
