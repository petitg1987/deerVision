import React, {Component, Suspense} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Switch, Route} from 'react-router-dom';
import Navigation from "./components/navigation/navigation";
import './index.css';

const Main = React.lazy(() => import('./pages/main/main'))
const Admin = React.lazy(() => import('./pages/admin/admin'))

class Index extends Component {

    constructor(props) {
        super(props);

        let date = new Date();
        this.currentYear = date.getFullYear();
    }

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
                    <footer>
                        <center><br/><small>&copy; Copyright {this.currentYear}, Deer Vision Studio</small><br/><br/></center>
                    </footer>
                </div>
            </BrowserRouter>
        );
    }
}

ReactDOM.render((
    <Index/>
), document.getElementById('app'))
