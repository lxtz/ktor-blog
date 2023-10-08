<script lang="ts">
    import {onMount} from "svelte";

    const clientHello = "Hello Client";
    let serverHello = "";

    onMount(async () => {
        const request = await fetch("/hello");
        serverHello = await request.text();
    })
</script>

<h1>Hello View</h1>
<h1>{clientHello}</h1>
<h1>{serverHello}</h1>