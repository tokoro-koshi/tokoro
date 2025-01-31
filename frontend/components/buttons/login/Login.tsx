import Link from "next/link";
import routes from "@/lib/constants/routes";
import styles from "@/components/buttons/login/login.module.css";

export default function LoginButton() {

    return (
        <Link href={routes.auth.login} className={styles.loginButton}>Login</Link>
    )
}