<script lang="ts">
    import type {PageData} from './$types';

    export let data: PageData;
</script>

<h2>Posts:</h2>
{#each data.posts as post}
    <p>
        <a href="/post/{post.id}">{post.title}</a> by {post.author}
    </p>
{/each}