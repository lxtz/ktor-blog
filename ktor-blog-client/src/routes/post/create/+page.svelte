<script lang="ts">

    import {goto} from "$app/navigation";
    import {authService} from "../../../hooks.client";
    import {onMount} from "svelte";

    let title: string;
    let author: string;
    let content: string;

    let invalid = false;

    onMount(async () => {
        if(!authService.isAuthenticated()) {
            await goto("/login");
        }
    });

    async function onCreateClick() {
        const response = await fetch("/api/posts/", {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                "Authorization": authService.getHeader()
            },
            body: JSON.stringify({
                title, author, content
            })
        })
        if (response.ok) {
            const id: string = await response.json();
            invalid = false;
            await goto(`/post/${id}`);
        } else {
            invalid = true;
            console.log("Error creating post:", response.statusText);
        }
    }
</script>

<h2>New post</h2>

<p>
    Author:<br/>
    <input type="text" bind:value={author}/>
</p>
<p>
    Title:<br/>
    <input type="text" bind:value={title}/>
</p>
<p>
    Content:<br/>
    <textarea rows="10" cols="40" bind:value={content}></textarea>
</p>
<p>
    <button on:click={onCreateClick}>Create</button>
</p>
{#if invalid}
    <p>Invalid post.</p>
{/if}