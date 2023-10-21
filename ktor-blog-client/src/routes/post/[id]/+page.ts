import type {PageLoad} from './$types';
import type {Post} from "$lib/models/post";

export const load: PageLoad = async ({params, fetch}) => {
    const post: Post = await (await fetch(`/api/posts/${params.id}`)).json();
    return {
        post
    };
};