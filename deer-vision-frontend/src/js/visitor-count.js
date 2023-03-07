import React, {useEffect} from 'react';
import {getBackendUrl} from "./access";

export default function VisitorCount() {

    useEffect(() => {
        fetch(getBackendUrl() + "api/visitor/add", {
            method: "POST",
        }).catch((error) => {
            console.log(error);
        });
    });

    return (<></>)
}
