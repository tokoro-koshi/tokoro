'use client';
import { useUser } from '@auth0/nextjs-auth0/client';
import LogoutButton from "@/components/buttons/logout/Logout";
import LoginButton from "@/components/buttons/login/Login";
import RegisterButton from "@/components/buttons/register/Register";

export default function AuthButtons() {
  const { user, isLoading } = useUser();
  if (isLoading) return null;
  
  return (
    <div>{user ? (
        <LogoutButton />
    ) : (
        <>
            <LoginButton />
            <RegisterButton />
        </>
    )}</div>
  );
}