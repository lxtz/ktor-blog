import type {PageLoad} from './$types';
import type {Post} from "$lib/models/post";

export const load: PageLoad = async ({fetch}) => {
    const posts: Post[] = await (await fetch("/api/posts")).json();
    return {
        posts
    };
};