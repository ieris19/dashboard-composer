# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
Types of changes

* Added for new features. 
* Changed for changes in existing functionality. 
* Deprecated for soon-to-be removed features. 
* Removed for now removed features. 
* Fixed for any bug fixes. 
* Security in case of vulnerabilities.

## [Unreleased] - [2.0.0] - 2024

### Added
- Background editing support
  - Users can now change the background of the dashboard
- New image editing panel

### Changed
- Displaced the Icon selection component to the image editing panel

## [1.1.0] - 2024-02-20

### Added
- New HTML Composer API
  - Jsoup HTML parser is now a dependency
  - DashboardBuilder, AssetManager and DashboardMetadata classes
- Clear button to clear selected link in the composer UI and clear editing state

### Changed
- Searching queries on the dashboard now clears the search bar after opening the
  search results

### Fixed
- Fixed a bug where the application was stuck in edit mode after a link's removal

## [1.0.0] - 2024-02-19

Initial release of the software. This marks the first full release and
sets the baseline of features and functionality. 