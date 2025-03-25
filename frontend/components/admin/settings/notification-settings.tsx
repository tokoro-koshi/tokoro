'use client';

import type React from 'react';

import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import { Separator } from '@/components/ui/separator';
import toast from 'react-hot-toast';

interface NotificationSettingsProps {
  settings: {
    email: boolean;
    push: boolean;
    slack: boolean;
    slackWebhook: string;
    notifyOnNewUser: boolean;
    notifyOnError: boolean;
  };
}

export default function NotificationSettings({
  settings,
}: NotificationSettingsProps) {
  const [formData, setFormData] = useState(settings);

  const updateSettingsMutation = useMutation({
    mutationFn: async (data: typeof formData) => {
      // This would be implemented using your settings API
      await new Promise((resolve) => setTimeout(resolve, 1000)); // Mock API call
      return data;
    },
    onSuccess: () => {
      toast.success(
        'Your notification settings have been updated successfully.'
      );
    },
  });

  const handleSwitchChange = (name: string) => (checked: boolean) => {
    setFormData((prev) => ({
      ...prev,
      [name]: checked,
    }));
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    updateSettingsMutation.mutate(formData);
  };

  return (
    <form onSubmit={handleSubmit} className='space-y-6'>
      <div className='space-y-4'>
        <div>
          <h3 className='text-lg font-medium'>Notification Channels</h3>
          <p className='text-sm text-muted-foreground'>
            Choose how you want to receive notifications.
          </p>
        </div>

        <div className='space-y-4'>
          <div className='flex items-center justify-between'>
            <div className='space-y-0.5'>
              <Label htmlFor='email'>Email Notifications</Label>
              <p className='text-sm text-muted-foreground'>
                Receive notifications via email.
              </p>
            </div>
            <Switch
              id='email'
              checked={formData.email}
              onCheckedChange={handleSwitchChange('email')}
            />
          </div>

          <div className='flex items-center justify-between'>
            <div className='space-y-0.5'>
              <Label htmlFor='push'>Push Notifications</Label>
              <p className='text-sm text-muted-foreground'>
                Receive push notifications in your browser.
              </p>
            </div>
            <Switch
              id='push'
              checked={formData.push}
              onCheckedChange={handleSwitchChange('push')}
            />
          </div>

          <div className='flex items-center justify-between'>
            <div className='space-y-0.5'>
              <Label htmlFor='slack'>Slack Notifications</Label>
              <p className='text-sm text-muted-foreground'>
                Receive notifications in Slack.
              </p>
            </div>
            <Switch
              id='slack'
              checked={formData.slack}
              onCheckedChange={handleSwitchChange('slack')}
            />
          </div>
        </div>

        {formData.slack && (
          <div className='space-y-2'>
            <Label htmlFor='slackWebhook'>Slack Webhook URL</Label>
            <Input
              id='slackWebhook'
              name='slackWebhook'
              value={formData.slackWebhook}
              onChange={handleInputChange}
              placeholder='https://hooks.slack.com/services/...'
            />
            <p className='text-xs text-muted-foreground'>
              Enter your Slack webhook URL to receive notifications.
            </p>
          </div>
        )}
      </div>

      <Separator />

      <div className='space-y-4'>
        <div>
          <h3 className='text-lg font-medium'>Notification Events</h3>
          <p className='text-sm text-muted-foreground'>
            Choose which events trigger notifications.
          </p>
        </div>

        <div className='space-y-4'>
          <div className='flex items-center justify-between'>
            <div className='space-y-0.5'>
              <Label htmlFor='notifyOnNewUser'>New User Registration</Label>
              <p className='text-sm text-muted-foreground'>
                Get notified when a new user registers.
              </p>
            </div>
            <Switch
              id='notifyOnNewUser'
              checked={formData.notifyOnNewUser}
              onCheckedChange={handleSwitchChange('notifyOnNewUser')}
            />
          </div>

          <div className='flex items-center justify-between'>
            <div className='space-y-0.5'>
              <Label htmlFor='notifyOnError'>System Errors</Label>
              <p className='text-sm text-muted-foreground'>
                Get notified when a system error occurs.
              </p>
            </div>
            <Switch
              id='notifyOnError'
              checked={formData.notifyOnError}
              onCheckedChange={handleSwitchChange('notifyOnError')}
            />
          </div>
        </div>
      </div>

      <Separator />

      <Button type='submit' disabled={updateSettingsMutation.isPending}>
        {updateSettingsMutation.isPending ? 'Saving...' : 'Save Changes'}
      </Button>
    </form>
  );
}
