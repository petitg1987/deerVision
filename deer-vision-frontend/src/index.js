import React, {Component, Suspense} from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import Navigation from "./components/navigation/navigation";
import Home from './pages/home/home';
import About from "./pages/about/about";
import Privacy from "./pages/privacy/privacy";
import Eula from "./pages/eula/eula";
import './index.css';
import SocialNetwork from "./components/social-network/social-network";

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
                        <br/><br/>
                        <Suspense fallback={<div>Loading...</div>}>
                            <Routes>
                                <Route exact path='/' element={<Home/>}/>
                                <Route exact path='/about' element={<About/>}/>
                                <Route exact path='/privacy' element={<Privacy/>}/>
                                <Route exact path='/eula' element={<Eula/>}/>
                                <Route exact path='/admin' element={<Admin/>}/>
                            </Routes>
                        </Suspense>
                    </div>
                    <footer>
                        <div className="soc-container">
                            <SocialNetwork logoSize={50} label="Join us on:" onlyCommunityNetwork={false}/>
                        </div>
                        <center>
                            <small>&copy; {this.currentYear}, Deer Vision Studio | <a className={"text-link"} href={"/privacy"} title={"Privacy Policy"}>Privacy</a> | <a className={"text-link"} href={"/eula"} title={"End User License Agreement"}>EULA</a></small>
                            <br/>
                            <br/>
                        </center>
                    </footer>
                </div>
            </BrowserRouter>
        );
    }
}

ReactDOM.render((
    <Index/>
), document.getElementById('app'))
