'use client';

import Link from 'next/link';
import Image from 'next/image';
import routes from '@/lib/constants/routes';
import { useUser } from '@/lib/stores/user';
import Logo from '@/public/logo.svg';
import styles from './header.module.css';
import {Button} from "@/components/buttons/button";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {
    DropdownMenu,
    DropdownMenuContent, DropdownMenuGroup, DropdownMenuItem, DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu"; 
import {ChevronRight, CreditCard, LogOut, Settings} from "lucide-react";
import {useRouter} from "next/navigation";

const baseRoutes = [{path:routes.home, label:"Home"},{path:routes.home+"#features", label:"Features"},{path:routes.home+"#about", label:"About"}];
const authRoutes = [{path:routes.home, label:"Home"},{path:routes.explore, label:"Explore"},{path:routes.blog, label:"Blog"},{path:routes.aiSearch, label:"AI Search"}];

export default function Header() {
    const router = useRouter();
    const { user, isLoading } = useUser();
    const currentRoutes = user ? authRoutes : baseRoutes;

    console.log(user?.picture);

    return (
        <header className={styles.header}>
            <Link href={routes.home} className={styles.logo}>
                <Image src={Logo} alt={'Tokoro Logo'} className={styles.logoImage} />
                <h1 className={styles.name}>Tokoro</h1>
            </Link>
            <nav className={styles.nav}>
                {currentRoutes.map((route, index) =>
                    (<Link key={index} href={route.path} className={styles.navLink}>
                        {route.label}
                    </Link>))}
            </nav>
            {!isLoading && (
                <div className={styles.authButtons}>
                    {user ?
                            <DropdownMenu>
                                <DropdownMenuTrigger asChild>
                                    <Avatar>
                                        <AvatarImage src={user?.picture??""}/>
                                        <AvatarFallback>{user?.name?.substring(0,3)}</AvatarFallback>
                                    </Avatar>
                                </DropdownMenuTrigger>

                                <DropdownMenuContent className="w-56 mr-2.5">
                                    <DropdownMenuLabel>{user?.name}</DropdownMenuLabel>
                                    <DropdownMenuSeparator />
                                    <DropdownMenuGroup>
                                        <DropdownMenuItem  onClick={()=>router.push(routes.settings)}>
                                            <Settings /> Settings
                                        </DropdownMenuItem>
                                        <DropdownMenuItem onClick={()=>router.push(routes.support)} className="flex justify-between">
                                            <CreditCard />
                                            <span>Support</span>
                                            <ChevronRight className="ml-auto"/>
                                        </DropdownMenuItem>
                                    </DropdownMenuGroup>
                                    <DropdownMenuSeparator />
                                    <DropdownMenuGroup>
                                        <DropdownMenuItem onClick={()=> window.location.href = routes.auth.logout}>
                                            <LogOut /> Logout
                                        </DropdownMenuItem>
                                    </DropdownMenuGroup>
                                </DropdownMenuContent>
                            </DropdownMenu>
                        :  (
                            <>
                                <a href={routes.auth.login} className={styles.navLink}>
                                    Login
                                </a>
                                <Button
                                    className={styles.roundedBtn}
                                    action={user ? routes.auth.logout : routes.auth.register}
                                >
                                    Sign up
                                </Button>
                            </>
                        )}
                </div>
            )}
        </header>
    );
}