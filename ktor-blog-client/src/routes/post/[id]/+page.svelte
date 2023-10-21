<script lang="ts">
    import type {PageData} from './$types';

    export let data: PageData;
</script>

<h2>{data.post.title}</h2>
<p>by {data.post.author}</p>
<p>{data.post.content}</p>