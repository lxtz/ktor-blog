export class AuthService {
    private token: string | null = null;
    private payload: Record<string, unknown> = {};

    constructor() {
        this.storeToken(localStorage.getItem("token"));
    }

    storeToken(token: string | null) {
        this.token = token;
        const tokenPayloadString = this.token?.split(".")[1];
        if (this.token !== null && tokenPayloadString) {
            localStorage.setItem("token", this.token);
            this.payload = JSON.parse(atob(tokenPayloadString));
        } else {
            localStorage.removeItem("token");
            this.payload = {};
        }
    }

    isAuthenticated(): boolean {
        return !!this.token;
    }

    getToken(): string | null {
        return this.token;
    }

    getHeader(): string {
        if (!this.token) return "";
        return `Bearer ${this.token}`;
    }

    getClaim(key: string): unknown {
        return this.payload[key];
    }
}