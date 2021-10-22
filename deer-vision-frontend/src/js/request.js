
export async function fetchWithTimeout(resource, options = {}) {
    const { timeout = 1000 /* default value */ } = options;

    const controller = new AbortController();
    const id = setTimeout(() => controller.abort(), timeout);
    const response = await fetch(resource, {
        ...options,
        signal: controller.signal
    });
    clearTimeout(id);
    return response;
}
