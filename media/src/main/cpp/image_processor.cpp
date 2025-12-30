#include "image_processor.h"
#include <algorithm>
#include <cstring>
#include <cmath>

// Helper to clamp values between 0 and 255
inline uint8_t clamp(int value) {
    return static_cast<uint8_t>(std::max(0, std::min(255, value)));
}

// Get pixel components from ARGB_8888 format
inline void getARGB(uint32_t pixel, uint8_t& a, uint8_t& r, uint8_t& g, uint8_t& b) {
    a = (pixel >> 24) & 0xFF;
    r = (pixel >> 16) & 0xFF;
    g = (pixel >> 8) & 0xFF;
    b = pixel & 0xFF;
}

// Create ARGB_8888 pixel from components
inline uint32_t makeARGB(uint8_t a, uint8_t r, uint8_t g, uint8_t b) {
    return (static_cast<uint32_t>(a) << 24) |
           (static_cast<uint32_t>(r) << 16) |
           (static_cast<uint32_t>(g) << 8) |
           static_cast<uint32_t>(b);
}

void applyGrayscale(void* pixels, int width, int height, int stride) {
    auto* pixelData = static_cast<uint8_t*>(pixels);
    
    for (int y = 0; y < height; y++) {
        auto* row = reinterpret_cast<uint32_t*>(pixelData + y * stride);
        for (int x = 0; x < width; x++) {
            uint8_t a, r, g, b;
            getARGB(row[x], a, r, g, b);
            
            // Use luminosity method for better grayscale
            uint8_t gray = static_cast<uint8_t>(0.299 * r + 0.587 * g + 0.114 * b);
            row[x] = makeARGB(a, gray, gray, gray);
        }
    }
}

void applySepia(void* pixels, int width, int height, int stride) {
    auto* pixelData = static_cast<uint8_t*>(pixels);
    
    for (int y = 0; y < height; y++) {
        auto* row = reinterpret_cast<uint32_t*>(pixelData + y * stride);
        for (int x = 0; x < width; x++) {
            uint8_t a, r, g, b;
            getARGB(row[x], a, r, g, b);
            
            // Sepia tone formula
            int newR = static_cast<int>(0.393 * r + 0.769 * g + 0.189 * b);
            int newG = static_cast<int>(0.349 * r + 0.686 * g + 0.168 * b);
            int newB = static_cast<int>(0.272 * r + 0.534 * g + 0.131 * b);
            
            row[x] = makeARGB(a, clamp(newR), clamp(newG), clamp(newB));
        }
    }
}

void applyInvert(void* pixels, int width, int height, int stride) {
    auto* pixelData = static_cast<uint8_t*>(pixels);
    
    for (int y = 0; y < height; y++) {
        auto* row = reinterpret_cast<uint32_t*>(pixelData + y * stride);
        for (int x = 0; x < width; x++) {
            uint8_t a, r, g, b;
            getARGB(row[x], a, r, g, b);
            row[x] = makeARGB(a, 255 - r, 255 - g, 255 - b);
        }
    }
}

void adjustBrightness(void* pixels, int width, int height, int stride, int factor) {
    auto* pixelData = static_cast<uint8_t*>(pixels);
    
    for (int y = 0; y < height; y++) {
        auto* row = reinterpret_cast<uint32_t*>(pixelData + y * stride);
        for (int x = 0; x < width; x++) {
            uint8_t a, r, g, b;
            getARGB(row[x], a, r, g, b);
            
            row[x] = makeARGB(a, 
                              clamp(r + factor), 
                              clamp(g + factor), 
                              clamp(b + factor));
        }
    }
}

void adjustContrast(void* pixels, int width, int height, int stride, float factor) {
    auto* pixelData = static_cast<uint8_t*>(pixels);
    
    for (int y = 0; y < height; y++) {
        auto* row = reinterpret_cast<uint32_t*>(pixelData + y * stride);
        for (int x = 0; x < width; x++) {
            uint8_t a, r, g, b;
            getARGB(row[x], a, r, g, b);
            
            // Contrast formula: (color - 128) * factor + 128
            int newR = static_cast<int>((r - 128) * factor + 128);
            int newG = static_cast<int>((g - 128) * factor + 128);
            int newB = static_cast<int>((b - 128) * factor + 128);
            
            row[x] = makeARGB(a, clamp(newR), clamp(newG), clamp(newB));
        }
    }
}

void rotate90CW(void* srcPixels, void* dstPixels, int width, int height, int srcStride, int dstStride) {
    auto* src = static_cast<uint8_t*>(srcPixels);
    auto* dst = static_cast<uint8_t*>(dstPixels);
    
    // For 90° CW rotation: dst(x, y) = src(height - 1 - y, x)
    // New dimensions: newWidth = height, newHeight = width
    for (int y = 0; y < height; y++) {
        auto* srcRow = reinterpret_cast<uint32_t*>(src + y * srcStride);
        for (int x = 0; x < width; x++) {
            int newX = height - 1 - y;
            int newY = x;
            auto* dstRow = reinterpret_cast<uint32_t*>(dst + newY * dstStride);
            dstRow[newX] = srcRow[x];
        }
    }
}

void rotate90CCW(void* srcPixels, void* dstPixels, int width, int height, int srcStride, int dstStride) {
    auto* src = static_cast<uint8_t*>(srcPixels);
    auto* dst = static_cast<uint8_t*>(dstPixels);
    
    // For 90° CCW rotation: dst(x, y) = src(y, width - 1 - x)
    // New dimensions: newWidth = height, newHeight = width
    for (int y = 0; y < height; y++) {
        auto* srcRow = reinterpret_cast<uint32_t*>(src + y * srcStride);
        for (int x = 0; x < width; x++) {
            int newX = y;
            int newY = width - 1 - x;
            auto* dstRow = reinterpret_cast<uint32_t*>(dst + newY * dstStride);
            dstRow[newX] = srcRow[x];
        }
    }
}

void rotate180(void* pixels, int width, int height, int stride) {
    auto* pixelData = static_cast<uint8_t*>(pixels);
    
    // For 180° rotation, we can do it in-place by swapping pixels
    for (int y = 0; y < height / 2; y++) {
        auto* topRow = reinterpret_cast<uint32_t*>(pixelData + y * stride);
        auto* bottomRow = reinterpret_cast<uint32_t*>(pixelData + (height - 1 - y) * stride);
        
        for (int x = 0; x < width; x++) {
            std::swap(topRow[x], bottomRow[width - 1 - x]);
        }
    }
    
    // Handle middle row for odd height
    if (height % 2 == 1) {
        int midY = height / 2;
        auto* midRow = reinterpret_cast<uint32_t*>(pixelData + midY * stride);
        for (int x = 0; x < width / 2; x++) {
            std::swap(midRow[x], midRow[width - 1 - x]);
        }
    }
}

void cropBitmap(void* srcPixels, void* dstPixels, int srcWidth, int srcHeight,
                int srcStride, int dstStride,
                int cropX, int cropY, int cropWidth, int cropHeight) {
    auto* src = static_cast<uint8_t*>(srcPixels);
    auto* dst = static_cast<uint8_t*>(dstPixels);
    
    for (int y = 0; y < cropHeight; y++) {
        auto* srcRow = reinterpret_cast<uint32_t*>(src + (cropY + y) * srcStride);
        auto* dstRow = reinterpret_cast<uint32_t*>(dst + y * dstStride);
        
        std::memcpy(dstRow, srcRow + cropX, cropWidth * sizeof(uint32_t));
    }
}
