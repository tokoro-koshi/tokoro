import { Suspense } from 'react';
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import GeneralSettings from '@/components/admin/settings/general-settings';
import ApiSettings from '@/components/admin/settings/api-settings';
import NotificationSettings from '@/components/admin/settings/notification-settings';
import SettingsSkeleton from '@/components/admin/settings/settings-skeleton';
import styles from '@/components/admin/admin.module.css';

// Server-side data fetching
async function fetchSettings() {
  // This would be implemented using your settings API
  // For now, returning default settings
  return {
    general: {
      siteName: 'Tokoro Admin',
      siteDescription: 'Admin panel for Tokoro',
      logo: '/logo.svg',
      favicon: '/favicon.ico',
      primaryColor: '#0070f3',
      itemsPerPage: 10,
    },
    api: {
      rateLimit: 100,
      timeout: 30,
      enableCaching: true,
      cacheDuration: 3600,
    },
    notifications: {
      email: true,
      push: false,
      slack: false,
      slackWebhook: '',
      notifyOnNewUser: true,
      notifyOnError: true,
    },
  };
}

export default async function SettingsPage() {
  const settings = await fetchSettings();
  return (
    <div>
      <h1 className={styles.pageTitle}>Settings</h1>
      <Suspense fallback={<SettingsSkeleton />}>
        <Tabs defaultValue='general' className={styles.tabsContainer}>
          <TabsList>
            <TabsTrigger value='general'>General</TabsTrigger>
            <TabsTrigger value='api'>API</TabsTrigger>
            <TabsTrigger value='notifications'>Notifications</TabsTrigger>
          </TabsList>

          <TabsContent value='general'>
            <Card>
              <CardHeader>
                <CardTitle>General Settings</CardTitle>
                <CardDescription>
                  Manage your application&#39;s general settings.
                </CardDescription>
              </CardHeader>
              <CardContent>
                <GeneralSettings settings={settings.general} />
              </CardContent>
            </Card>
          </TabsContent>

          <TabsContent value='api'>
            <Card>
              <CardHeader>
                <CardTitle>API Settings</CardTitle>
                <CardDescription>
                  Configure API settings and manage access tokens.
                </CardDescription>
              </CardHeader>
              <CardContent>
                <ApiSettings settings={settings.api} />
              </CardContent>
            </Card>
          </TabsContent>

          <TabsContent value='notifications'>
            <Card>
              <CardHeader>
                <CardTitle>Notification Settings</CardTitle>
                <CardDescription>
                  Configure how notifications are sent and received.
                </CardDescription>
              </CardHeader>
              <CardContent>
                <NotificationSettings settings={settings.notifications} />
              </CardContent>
            </Card>
          </TabsContent>
        </Tabs>
      </Suspense>
    </div>
  );
}
