
export async function fetchWithTimeout(resource, options = {}) {
    const { timeout = 2500 /* default value */ } = options;

    const controller = new AbortController();
    const id = setTimeout(() => controller.abort(), timeout);
    const response = await fetch(resource, {
        ...options,
        signal: controller.signal
    });
    clearTimeout(id);
    return response;
}

export async function postRequest(resource) {
    let fetchResult = await fetchWithTimeout(resource, {
        method: 'POST'
    });
    return await fetchResult.json();
}

export async function getWithToken(resource, token) {
    let fetchResult = await fetchWithTimeout(resource, {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
        })
    });
    return await fetchResult.json();
}

export async function deleteWithToken(resource, token) {
    return await fetchWithTimeout(resource, {
        method: 'DELETE',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
        })
    });
}
