<script lang="ts">

    import {authService} from "../../hooks.client";

    let username = "";
    let password = "";

    let token = authService.getToken() ?? "";
    let error = "";

    let headers;
    let payload;

    $: headers = token && atob(token.split(".")[0]);
    $: payload = token && atob(token.split(".")[1]);

    async function onLoginClick() {
        const response = await fetch("/login/", {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({username, password})
        })
        if (response.ok) {
            const tokenResponse: { token: string } = await response.json();
            error = "";
            token = tokenResponse.token;
            authService.storeToken(token);
        } else {
            error = response.statusText;
            token = "";
            console.log("Error logging in:", response.statusText);
        }
    }

    async function onLogoutClick() {
        const response = await fetch("/logout/", {
            method: 'POST'
        })
        authService.storeToken(null);
        if (response.ok) {
            error = "";
            token = "";
        } else {
            error = response.statusText;
            token = "";
            console.log("Error logging out:", response.statusText);
        }
    }
</script>

<h2>Login</h2>

<p>
    Username:<br/>
    <input type="text" bind:value={username}/>
</p>
<p>
    Password:<br/>
    <input type="password" bind:value={password}/>
</p>

<button on:click={onLoginClick}>Log in</button>&nbsp;
<button on:click={onLogoutClick}>Log out</button>

<p>Token:</p>
<pre>{token}</pre>

<p>Headers:</p>
<pre>{headers}</pre>

<p>Payload:</p>
<pre>{payload}</pre>

<p>{error}</p>