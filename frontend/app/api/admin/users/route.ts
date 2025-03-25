import { type NextRequest, NextResponse } from 'next/server';
import { UserClient } from '@/lib/requests/user.client';
import type { Pagination } from '@/lib/types/pagination';
import type { User } from '@/lib/types/user';

// This is a server-side route handler for admin API endpoints
export async function GET(request: NextRequest) {
  try {
    // Get query parameters
    const searchParams = request.nextUrl.searchParams;
    const page = Number.parseInt(searchParams.get('page') || '0', 10);
    const size = Number.parseInt(searchParams.get('size') || '10', 10);

    // Fetch users from the API
    const usersResponse: Pagination<User> = await UserClient.getUsers(
      page,
      size,
    );

    // Return the paginated response
    return NextResponse.json({
      data: usersResponse.payload,
      pagination: {
        page: usersResponse.meta.number,
        size: usersResponse.meta.size,
        total: usersResponse.meta.totalElements,
        totalPages: usersResponse.meta.totalPages,
      },
    });
  } catch (error) {
    console.error('Error fetching users:', error);
    return NextResponse.json(
      { error: 'Failed to fetch users' },
      { status: 500 }
    );
  }
}

// Create a new user
export async function POST(request: NextRequest) {
  try {
    // Parse request body
    const userData = await request.json();

    // Validate required fields
    if (!userData.email || !userData.name) {
      return NextResponse.json(
        { error: 'Email and name are required' },
        { status: 400 }
      );
    }

    // Create user
    const newUser = await UserClient.createUser(userData);

    return NextResponse.json(newUser, { status: 201 });
  } catch (error) {
    console.error('Error creating user:', error);
    return NextResponse.json(
      { error: 'Failed to create user' },
      { status: 500 }
    );
  }
}

// Update a user
export async function PUT(request: NextRequest) {
  try {
    // Parse request body
    const userData = await request.json();

    // Validate required fields
    if (!userData.id) {
      return NextResponse.json(
        { error: 'User ID is required' },
        { status: 400 }
      );
    }

    // Update user
    const updatedUser = await UserClient.updateUser(userData.id, userData);

    return NextResponse.json(updatedUser);
  } catch (error) {
    console.error('Error updating user:', error);
    return NextResponse.json(
      { error: 'Failed to update user' },
      { status: 500 }
    );
  }
}

// Delete a user
export async function DELETE(request: NextRequest) {
  try {
    // Get user ID from query parameters
    const searchParams = request.nextUrl.searchParams;
    const userId = searchParams.get('id');

    if (!userId) {
      return NextResponse.json(
        { error: 'User ID is required' },
        { status: 400 }
      );
    }

    // Delete user
    await UserClient.deleteUser(userId);

    return NextResponse.json({ success: true });
  } catch (error) {
    console.error('Error deleting user:', error);
    return NextResponse.json(
      { error: 'Failed to delete user' },
      { status: 500 }
    );
  }
}
